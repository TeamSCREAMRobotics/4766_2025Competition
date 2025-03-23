// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Manipulator;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsytems.manipulator.Manipulator;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ManipPivot extends Command {
  private Manipulator manipulator;
  double setpoint;
  boolean holdManip = false;

  /** Creates a new manipulatorPivot. */
  public ManipPivot(Manipulator manipulator, double Setpoint, boolean hold) {
    this.manipulator = manipulator;
    setpoint = Setpoint;
    holdManip = hold;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(manipulator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    manipulator.goToSetpoint(setpoint);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return manipulator.atSetpoint(setpoint, 0.3) && !holdManip;
  }
}
