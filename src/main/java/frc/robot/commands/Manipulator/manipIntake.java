// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Manipulator;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsytems.Manipulator;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class manipIntake extends Command {
  Manipulator s_Manipulator;
  private boolean trigger;
  private Timer timer = new Timer();
  private BooleanSupplier conA;
  private BooleanSupplier conB;

  /** Creates a new manip. */
  public manipIntake(Manipulator manipulator, BooleanSupplier conA, BooleanSupplier conB) {
    s_Manipulator = manipulator;
    this.conA = conA;
    this.conB = conB;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(s_Manipulator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    trigger = s_Manipulator.laserPassed();
    s_Manipulator.runFeedMotor(.7);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (trigger && conA.getAsBoolean() == true) {
      s_Manipulator.runFeedMotor(-7);
      timer.reset();
    } 
    if (!trigger && conA.getAsBoolean() == true) {
      s_Manipulator.runFeedMotor(7);
      Timer.delay(1);
    }
    if (!trigger && conA.getAsBoolean() == false) {
      s_Manipulator.runFeedMotor(.7);
    }
    if (trigger) {
      timer.start();
    }
    if(conB.getAsBoolean()) {
      s_Manipulator.goToSetpoint(5.4);
    }
    if (!conB.getAsBoolean()) {
      s_Manipulator.goToSetpoint(0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_Manipulator.stopFeed();
    trigger = false;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (trigger == true) {
      return timer.hasElapsed(0.5);
    } else {
      return s_Manipulator.laserPassed();
    }
  }
}
