// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc2025.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc2025.robot.constants.Constants.ElevatorConstants;
import frc2025.robot.constants.Constants.ManipulatorConstants;
import frc2025.robot.subsytems.Elevator;
import frc2025.robot.subsytems.manipulator.Manipulator;
import frc2025.robot.subsytems.manipulator.ManipulatorFeeder;
import java.util.function.BooleanSupplier;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class AutoLoad extends Command {
  /** Creates a new AutoLoad. */
  private Manipulator manipulator;

  private Elevator elevator;
  private ManipulatorFeeder feeder;
  private BooleanSupplier range;
  private boolean done;

  public AutoLoad(Manipulator manipulator, Elevator elevator, ManipulatorFeeder feeder) {
    this.manipulator = manipulator;
    this.elevator = elevator;
    this.feeder = feeder;
    range = () -> manipulator.laserPassed();
    done = false;

    addRequirements(manipulator, elevator, feeder);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!range.getAsBoolean()) {
      manipulator.goToSetpoint(ManipulatorConstants.intakeSetpoint);
      elevator.goToSetPoint(ElevatorConstants.loadingSetpoint);
      feeder.feed(5);
    }

    if (range.getAsBoolean()) {
      elevator.goToSetPoint(0);
      feeder.idleFeed();
      done = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return done;
  }
}
