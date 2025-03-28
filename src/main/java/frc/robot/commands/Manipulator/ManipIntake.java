// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Manipulator;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsytems.Elevator;
import frc.robot.subsytems.manipulator.Manipulator;
import frc.robot.subsytems.manipulator.ManipulatorFeeder;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ManipIntake extends Command {
  private Manipulator manipulator;
  private ManipulatorFeeder manipFeed;
  private Elevator elevator;
  private boolean trigger;
  private Timer timer = new Timer();

  /** Creates a new manip. */
  public ManipIntake(Manipulator manipulator, Elevator elevator, ManipulatorFeeder manipFeed) {
    this.manipulator = manipulator;
    this.elevator = elevator;
    this.manipFeed = manipFeed;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(manipulator, manipFeed);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    trigger = manipulator.laserPassed();
    if (trigger) {
      if (elevator.elevatorState == 1) {
        manipFeed.feed(-4);
      } else manipFeed.feed(-3);
      timer.reset();
    } else {
      manipFeed.feed(7);
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (trigger) {
      timer.start();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    manipFeed.stopFeed();
    trigger = false;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (trigger == true) {
      return timer.hasElapsed(0.5);
    } else {
      return manipulator.laserPassed();
    }
  }
}
