// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Elevator;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsytems.Elevator;
import frc.robot.subsytems.manipulator.Manipulator;
import java.util.function.BooleanSupplier;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class RunElevator extends Command {
  Elevator s_Elevator;
  double Setpoint;
  private final Manipulator manipulator;
  private double manipSetpoint;
  BooleanSupplier trigger;

  /** Creates a new runElevator. */
  public RunElevator(
      Elevator elevator,
      double setpoint,
      Manipulator manipulator,
      double manipSetpoint,
      BooleanSupplier triggSupplier) {
    s_Elevator = elevator;
    Setpoint = setpoint;
    this.manipulator = manipulator;
    this.manipSetpoint = manipSetpoint;
    trigger = triggSupplier;
    s_Elevator.elevatorState = s_Elevator.elevatorState;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(s_Elevator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    s_Elevator.elevatorState = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!manipulator.laserPassed() && s_Elevator.elevatorState == 0) {
      s_Elevator.elevatorState = 2;
    }

    if (s_Elevator.elevatorState == 0) {
      // manipulator.goToSetpoint(0);
      s_Elevator.goToSetPoint(Setpoint);
      s_Elevator.elevatorState = 1;
    }

    if (s_Elevator.elevatorState == 1) {
      //manipulator.goToSetpoint(manipSetpoint);
      s_Elevator.goToSetPoint(Setpoint);
    }

    if (s_Elevator.elevatorState == 1 && trigger.getAsBoolean() == true) {
      Timer.delay(0.1);
      //manipulator.goToSetpoint(0);
      s_Elevator.goToSetPoint(Setpoint);
      s_Elevator.elevatorState = 2;
    }

    if (s_Elevator.elevatorState == 2) {
      s_Elevator.goToSetPoint(0);
      //manipulator.goToSetpoint(0);
      s_Elevator.elevatorState = 3;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_Elevator.stopElevatorMotor();
    s_Elevator.elevatorState = 0;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return s_Elevator.atSetpoint(0.0) && s_Elevator.elevatorState == 3;
  }
}
