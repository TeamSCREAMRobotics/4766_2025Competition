// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Manipulator;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.constants.Constants.ManipulatorConstants;
import frc.robot.subsytems.manipulator.Manipulator;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ManipMain extends Command {
  private Manipulator manipulator;
  private boolean range;
  private CommandXboxController xcon;

  /** Creates a new ManipMain. */
  public ManipMain(Manipulator manipulator, CommandXboxController xcon) {
    this.manipulator = manipulator;
    this.xcon = xcon;
    range = manipulator.laserPassed();

    addRequirements(manipulator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // TODO: This will most likely break, fix later
    if (!range) {
      manipulator.goToSetpoint(ManipulatorConstants.intakeSetpoint);
    }

    if (range) {
      manipulator.goToSetpoint(ManipulatorConstants.levelFourSetpoint);
    }

    if (range && xcon.povLeft().getAsBoolean()) {
      manipulator.goToSetpoint(ManipulatorConstants.levelTwoSetpoint);
    }

    if (range && xcon.povUp().getAsBoolean()) {
      manipulator.goToSetpoint(ManipulatorConstants.levelFourSetpoint);
    }

    if (xcon.povDown().getAsBoolean()) {
      manipulator.goToSetpoint(ManipulatorConstants.algaeRemovalSetpoint);
    }

    if (!range && !xcon.povDown().getAsBoolean()) {
      manipulator.goToSetpoint(ManipulatorConstants.intakeSetpoint);
    }

    if (range && !xcon.povDown().getAsBoolean()) {
      manipulator.goToSetpoint(ManipulatorConstants.levelTwoSetpoint);
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
