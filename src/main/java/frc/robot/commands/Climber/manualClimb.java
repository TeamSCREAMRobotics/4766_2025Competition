// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Climber;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.controls.Controls;
import frc.robot.subsytems.Climber;

public class manualClimb extends Command {
  /** Creates a new manualClimb. */
  Climber s_Climber;

  DoubleSupplier joy;

  public manualClimb(Climber climber, DoubleSupplier Joy) {
    joy = Joy;
    s_Climber = climber;

    addRequirements(climber);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (Controls.eSwitch().getAsBoolean() == true) {
      double percent = 0;
    if(joy.getAsDouble() > 0.4 || joy.getAsDouble() < -0.4){
      percent = joy.getAsDouble() * 0.8;
    }
    s_Climber.manualClimb(percent);
    }
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
