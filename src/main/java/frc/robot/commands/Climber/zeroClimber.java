// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsytems.Climber;

public class zeroClimber extends Command {
  Climber s_Climber;

  /** Creates a new zeroClimber. */
  public zeroClimber(Climber climber) {
    s_Climber = climber;

    addRequirements(climber);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    s_Climber.zeroClimber();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
