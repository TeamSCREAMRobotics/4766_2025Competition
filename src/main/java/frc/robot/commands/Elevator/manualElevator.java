// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Elevator;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.controls.Controls;
import frc.robot.subsytems.Elevator;
import java.util.function.DoubleSupplier;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class manualElevator extends Command {
  Elevator s_Elevator;

  DoubleSupplier joy;

  /** Creates a new manualElevator. */
  public manualElevator(Elevator elevator, DoubleSupplier Joy) {
    joy = Joy;

    s_Elevator = elevator;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(elevator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (Controls.eSwitch().getAsBoolean() == false) {
      double percent = 0;
      if (joy.getAsDouble() > 0.4 || joy.getAsDouble() < -0.4) {
        percent = joy.getAsDouble() * 0.8;
      }
      s_Elevator.manualElevatorMotor(percent);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_Elevator.stopElevatorMotor();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
