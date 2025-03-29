// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsytems.manipulator.ManipulatorFeeder;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ManipIdle extends Command {
  private ManipulatorFeeder feeder;
  private BooleanSupplier range;
  public DoubleSupplier manipPivot;

  /** Creates a new ManipIdle. */
  public ManipIdle(ManipulatorFeeder feeder, BooleanSupplier range, DoubleSupplier manipPivot) {
    this.feeder = feeder;
    this.range = range;
    this.manipPivot = manipPivot;
    addRequirements(feeder);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!range.getAsBoolean() && manipPivot.getAsDouble() <= -6.5) {
      feeder.feed(5);
    } else {
      feeder.idleFeed();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
