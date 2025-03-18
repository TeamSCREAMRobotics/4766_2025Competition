// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Elevator;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsytems.Elevator;
import frc.robot.subsytems.Manipulator;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class AutoFlick extends Command {
  /** Creates a new algaeFlick. */
  private Elevator elevator;

  private Manipulator manipulator;
  private boolean L3True = true;
  private int state = 0;

  public AutoFlick(Elevator elevator, Manipulator manipulator, boolean L3True) {
    this.elevator = elevator;
    this.manipulator = manipulator;
    this.L3True = L3True;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(elevator, manipulator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (state == 0) {
      if (L3True) {
        manipulator.goToSetpoint(5.56);
        Timer.delay(.5);
        elevator.goToSetPoint(13.0);
        state = 1;
      } else {
        manipulator.goToSetpoint(5.56);
        state = 1;
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if (state == 1 && elevator.atSetpoint(13.0)) {
      if (L3True) {
        if (manipulator.laserPassed()) {
          elevator.goToSetPoint(13.8);
        }
        elevator.goToSetPoint(13.5);
        state = 2;
      } else {
        manipulator.goToSetpoint(0);
        state = 3;
      }
    }

    if (state == 2) {
      manipulator.goToSetpoint(0);
      Timer.delay(.17);
      elevator.goToSetPoint(0);
      state = 3;
    }

    if (state == 3) {
      Timer.delay(.5);
      state = 0;
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return state == 3;
  }
}
