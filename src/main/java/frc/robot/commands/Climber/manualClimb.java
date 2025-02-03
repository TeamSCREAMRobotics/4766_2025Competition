// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsytems.Climber;

public class manualClimb extends Command {
  /** Creates a new manualClimb. */
  Climber s_Climber;

  double voltage;

  public manualClimb(Climber climber, double Voltage) {
    voltage = Voltage;
    s_Climber = climber;

    addRequirements(climber);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    s_Climber.manualClimb(-voltage);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_Climber.resetManualClimb();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
