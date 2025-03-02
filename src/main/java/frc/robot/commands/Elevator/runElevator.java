// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Elevator;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants.ManipulatorConstants;
import frc.robot.subsytems.Elevator;
import frc.robot.subsytems.Manipulator;
import java.util.function.BooleanSupplier;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class runElevator extends Command {
  Elevator s_Elevator;
  double Setpoint;
  private final Manipulator manipulator;
  private double manipSetpoint = ManipulatorConstants.manipSetpoint;
  int state = s_Elevator.elevatorState;
  BooleanSupplier trigger;

  /** Creates a new runElevator. */
  public runElevator(
      Elevator elevator, double setpoint, Manipulator manipulator, BooleanSupplier triggSupplier) {
    s_Elevator = elevator;
    Setpoint = setpoint;
    this.manipulator = manipulator;
    trigger = triggSupplier;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(s_Elevator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    state = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!manipulator.laserPassed() && state == 0) {
      state = 3;
    }

    if (state == 0) {
      manipulator.runManip(0);
      s_Elevator.goToSetPoint(Setpoint);
      state = 1;
    }

    if (state == 1) {
      manipulator.runManip(manipSetpoint);
      s_Elevator.goToSetPoint(Setpoint);
    }

    if (state == 1 && trigger.getAsBoolean() == true) {
      state = 2;
      Timer.delay(0.1);
      manipulator.runManip(0);
      s_Elevator.goToSetPoint(Setpoint);
      state = 3;
    }

    if (state == 3) {
      s_Elevator.goToSetPoint(0);
      manipulator.runManip(0);
      state = 4;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_Elevator.stopElevatorMotor();
    state = 0;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return s_Elevator.atSetpoint(0.0) && state == 4;
  }
}
