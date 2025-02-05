// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Elevator;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsytems.Elevator;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class runElevator extends Command {
  Elevator s_Elevator;
  double Setpoint;

  /** Creates a new runElevator. */
  public runElevator(Elevator elevator, double setpoint) {
    elevator = s_Elevator;
    setpoint = Setpoint;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(s_Elevator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    s_Elevator.goToSetPoint(Setpoint);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_Elevator.stopElevatorMotor();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return s_Elevator.getPose() == Setpoint;
  }
}
