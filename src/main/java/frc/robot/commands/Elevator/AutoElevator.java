// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Elevator;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants.ManipulatorConstants;
import frc.robot.subsytems.Elevator;
import frc.robot.subsytems.Manipulator;
import frc.robot.subsytems.ManipulatorFeeder;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class AutoElevator extends Command {
  private Elevator elevator;
  private double setpoint;
  private Manipulator manipulator;
  private ManipulatorFeeder manipFeed;
  private double manipSetpoint = ManipulatorConstants.manipSetpoint;
  int state = 0;

  /** Creates a new runElevator. */
  public AutoElevator(
      Elevator elevator, ManipulatorFeeder manipFeed, Manipulator manipulator, double setpoint) {
    this.elevator = elevator;
    this.setpoint = setpoint;
    this.manipulator = manipulator;
    this.manipFeed = manipFeed;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(elevator, manipulator, manipFeed);
  }

  public AutoElevator(Elevator s_Elevator, double l3setpoint, Manipulator s_Manipulator) {
    // TODO Auto-generated constructor stub
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
      manipFeed.idleFeed();
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

      elevator.stopElevatorMotor();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return state == 3 && manipulator.laserPassed();
  }
}
