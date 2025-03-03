// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Elevator;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsytems.Elevator;
import frc.robot.subsytems.Intake;
import frc.robot.subsytems.Manipulator;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class algaeFlick extends Command {
  /** Creates a new algaeFlick. */
  private Elevator elevator;

  private Manipulator manipulator;
  private Intake intake;
  private boolean L3True = true;

  public algaeFlick(Elevator elevator, Manipulator manipulator, Intake intake, boolean L3True) {
    this.elevator = elevator;
    this.manipulator = manipulator;
    this.intake = intake;
    this.L3True = L3True;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(elevator, manipulator, intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (L3True) {
      manipulator.runManip(5.5);
      // Timer.delay(.5);
      elevator.goToSetPoint(17.5);
    } else {
      manipulator.runManip(5.5);
      intake.runFlywheel(1);
      intake.goToSetpoint(-0.2);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    manipulator.runManip(0);
    if (manipulator.getPose() <= 2.5) {
      elevator.goToSetPoint(0);
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
