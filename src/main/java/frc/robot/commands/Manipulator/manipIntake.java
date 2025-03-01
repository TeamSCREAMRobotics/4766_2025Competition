// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Manipulator;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsytems.Manipulator;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class manipIntake extends Command {
  Manipulator s_Manipulator;

  /** Creates a new manip. */
  public manipIntake(Manipulator manipulator) {
    s_Manipulator = manipulator;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(s_Manipulator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    s_Manipulator.runFeedMotor(5);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    System.out.println(s_Manipulator.laserPassed());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_Manipulator.stopFeed();
    System.out.println("The manipulator has been stopped");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return s_Manipulator.laserPassed();
  }
}
