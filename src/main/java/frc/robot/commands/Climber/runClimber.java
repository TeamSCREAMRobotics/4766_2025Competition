// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsytems.Climber;

public class runClimber extends Command {
  Climber s_Climber;
  double setpoint;

  /** Creates a new runClimber. */
  public runClimber(Climber climber, Double Setpoint) {
    s_Climber = climber;
    Setpoint = setpoint;

    addRequirements(climber);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    s_Climber.goToSetPoint(setpoint);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return s_Climber.isAtSetPoint(setpoint);
  }
}
