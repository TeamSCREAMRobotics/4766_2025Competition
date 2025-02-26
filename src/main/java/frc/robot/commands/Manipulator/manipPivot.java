// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Manipulator;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsytems.Manipulator;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class manipPivot extends Command {
  Manipulator s_Manipulator;
  double setpoint;

  /** Creates a new manipulatorPivot. */
  public manipPivot(Manipulator manipulator, double Setpoint) {
    s_Manipulator = manipulator;
    setpoint = Setpoint;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(s_Manipulator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    s_Manipulator.runManip(setpoint);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_Manipulator.stopManip();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return s_Manipulator.atSetpoint(setpoint);
  }
}
