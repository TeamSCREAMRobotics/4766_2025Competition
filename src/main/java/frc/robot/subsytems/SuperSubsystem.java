// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsytems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsytems.manipulator.Manipulator;

public class SuperSubsystem extends SubsystemBase {
  private Manipulator manipulator;
  private Elevator elevator;

  public void manipGoTo(double setpoint) {
    manipulator.goToSetpoint(setpoint);
  }

  public void elevatorGoTo(double setpoint) {
    elevator.goToSetPoint(setpoint);
  }

  public boolean manipAtSetpoint(double setpoint, double deadzone) {
    return manipulator.atSetpoint(setpoint, deadzone);
  }

  public boolean elevatorAtSetpoint(double setpoint, double deadzone) {
    return elevator.atSetpoint(setpoint, deadzone);
  }

  public boolean manipCANrange() {
    return manipulator.laserPassed();
  }

  public int elevatorState = 0;
}
