// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc2025.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc2025.robot.constants.Constants.ManipulatorConstants;
import frc2025.robot.subsytems.Climber;
import frc2025.robot.subsytems.manipulator.Manipulator;
import java.util.function.BooleanSupplier;

public class RunClimber extends Command {
  Climber s_Climber;
  Manipulator s_Pivot;
  double setpoint;
  BooleanSupplier buttonA;

  /** Creates a new runClimber. */
  public RunClimber(
      Climber climber, Manipulator manipPivot, double Setpoint, BooleanSupplier buttonA) {
    s_Climber = climber;
    setpoint = Setpoint;
    this.buttonA = buttonA;
    s_Pivot = manipPivot;

    addRequirements(climber, manipPivot);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // This should be the same setpoint as where the cage ends up.

    // Sends pivot out.
    s_Pivot.goToSetpoint(15);
    if (buttonA.getAsBoolean() && s_Pivot.getPosition() > 10) {
      // Cage grabbing point.
      s_Climber.goToSetPoint(setpoint);
    }

    if (!buttonA.getAsBoolean() && s_Pivot.getPosition() > 10) {
      s_Climber.goToSetPoint(60);
    }
  }

  @Override
  public void end(boolean interrupted) {
    s_Climber.goToSetPoint(0.0);
    if (s_Climber.getPosition() < 0.5) {
      s_Pivot.goToSetpoint(ManipulatorConstants.clearZoneSetpoint);
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
