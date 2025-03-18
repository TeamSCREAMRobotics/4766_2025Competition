// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Manipulator;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsytems.Manipulator;
import frc.robot.subsytems.ManipulatorFeeder;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class AutoScore extends Command {
  private Manipulator manipulator;
  private ManipulatorFeeder manipFeed;
  private int state = 0;

  /** Creates a new autoScore. */
  public AutoScore(Manipulator manipulator, ManipulatorFeeder manipFeed) {
    this.manipulator = manipulator;
    this.manipFeed = manipFeed;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(manipulator, manipFeed);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    state = 1;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (state == 1) {
      manipulator.goToSetpoint(5);
      state = 2;
    }

    if (state == 2 && manipulator.atSetpoint(5, 0.3)) {
      manipFeed.feed(-7);
      state = 3;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    state = 0;
    if (state == 0) {
      manipFeed.stopFeed();

      manipulator.goToSetpoint(0);
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return state == 3 && !manipulator.laserPassed();
  }
}
