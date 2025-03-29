// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc2025.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc2025.robot.constants.Constants.ElevatorConstants;
import frc2025.robot.constants.Constants.ManipulatorConstants;
import frc2025.robot.subsytems.Elevator;
import frc2025.robot.subsytems.manipulator.Manipulator;
import frc2025.robot.subsytems.manipulator.ManipulatorFeeder;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class AutoLevelFour extends Command {
  private Elevator elevator;
  private Manipulator manipulator;
  private ManipulatorFeeder feeder;

  /** Creates a new AutoLevelFour. */
  public AutoLevelFour(Elevator elevator, Manipulator manipulator, ManipulatorFeeder feeder) {
    this.elevator = elevator;
    this.manipulator = manipulator;
    this.feeder = feeder;

    addRequirements(elevator, manipulator, feeder);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // just... don't ask, it might be nested if statments but, who cares
    if (manipulator.laserPassed()) {
      manipulator.goToSetpoint(ManipulatorConstants.clearZoneSetpoint);
      if (manipulator.atSetpoint(ManipulatorConstants.clearZoneSetpoint, .2)) {
        elevator.goToSetPoint(ElevatorConstants.L4Setpoint);
        if (elevator.atSetpoint(ElevatorConstants.L4Setpoint, .2)) {
          manipulator.goToSetpoint(ManipulatorConstants.levelFourSetpoint);
          if (manipulator.atSetpoint(ManipulatorConstants.levelFourSetpoint, .2)) {
            feeder.feed(-3);
            if (!manipulator.laserPassed()) {
              Timer.delay(.2);
              manipulator.goToSetpoint(ManipulatorConstants.clearZoneSetpoint);
              if (manipulator.atSetpoint(ManipulatorConstants.clearZoneSetpoint, .2)) {
                elevator.goToSetPoint(0);
              }
            }
          }
        }
      }
    } else {
      manipulator.goToSetpoint(ManipulatorConstants.clearZoneSetpoint);
      elevator.goToSetPoint(0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return !manipulator.laserPassed() && elevator.atSetpoint(0, .2);
  }
}
