// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Elevator;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants.ManipulatorConstants;
import frc.robot.subsytems.Elevator;
import frc.robot.subsytems.Manipulator;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class autoElevator extends Command {
  Elevator elevator;
  double setpoint;
  private final Manipulator manipulator;
  private double manipSetpoint = ManipulatorConstants.manipSetpoint;
  int state = 0;

  /** Creates a new runElevator. */
  public autoElevator(Elevator elevator, double setpoint, Manipulator manipulator) {
    this.elevator = elevator;
    this.setpoint = setpoint;
    this.manipulator = manipulator;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(elevator, manipulator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    state = 1;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    if (state == 1) {
      manipulator.goToSetpoint(manipSetpoint);
      elevator.goToSetPoint(setpoint);
      state = 2;
    }

    if (state == 2 && elevator.atSetpoint(setpoint)) {
      manipulator.runFeedMotor(-7);
      state = 3;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    state = 0;

    if (state == 0) {
      manipulator.goToSetpoint(0);
      elevator.goToSetPoint(0);

      manipulator.stopManip();
      elevator.stopElevatorMotor();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return state == 3 && manipulator.laserPassed();
  }
}
