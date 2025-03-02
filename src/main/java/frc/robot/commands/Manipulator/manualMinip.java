// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Manipulator;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsytems.Manipulator;
import java.util.function.DoubleSupplier;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class manualMinip extends Command {
  /** Creates a new manualMinip. */
  Manipulator s_Manipulator;

  DoubleSupplier joy;

  public manualMinip(Manipulator manipulator, DoubleSupplier Joy) {
    s_Manipulator = manipulator;
    joy = Joy;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(manipulator);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // if (Controls.eSwitch().getAsBoolean() == true) {
    //   double percent = 0;
    //   if (joy.getAsDouble() > 0.4 || joy.getAsDouble() < -0.4) {
    //     percent = joy.getAsDouble() * 0.8;
    //   }
    //   s_Manipulator.manualManip(percent);
    // }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
