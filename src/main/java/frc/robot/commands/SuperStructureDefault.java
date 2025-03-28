// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.constants.Constants.ElevatorConstants;
import frc.robot.constants.Constants.ManipulatorConstants;
import frc.robot.subsytems.SuperSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class SuperStructureDefault extends Command {
  private SuperSubsystem ssuper;
  private CommandXboxController oXCon;
  private boolean range;
  /** Creates a new SuperStructureDefault. */
  public SuperStructureDefault(SuperSubsystem ssuper, CommandXboxController oXCon) {
    this.ssuper = ssuper;
    this.oXCon = oXCon;
    range = ssuper.manipCANrange();
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(ssuper);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    ssuper.elevatorState = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!range && ssuper.elevatorState == 0) {
      ssuper.manipGoTo(ManipulatorConstants.intakeSetpoint);
      ssuper.elevatorGoTo(ElevatorConstants.idleSetpoint);
      ssuper.elevatorState = 1;
    }

    if(range && ssuper.elevatorState == 1) {
      ssuper.manipGoTo(ManipulatorConstants.levelThreeSetpoint);

      if (!range) {
        Timer.delay(.2);
        ssuper.elevatorState = 0;
      }
    }

    if(range && oXCon.povLeft().getAsBoolean() && ssuper.elevatorState == 1) {
      ssuper.manipGoTo(ManipulatorConstants.levelTwoSetpoint);
      if (!range) {
        Timer.delay(.2);
        ssuper.elevatorState = 0;
      }
    }

    // do not look at jank code, it scares me
    if (range && oXCon.povUp().getAsBoolean() && ssuper.elevatorState == 1) {
      ssuper.manipGoTo(ManipulatorConstants.clearZoneSetpoint);
      if (ssuper.manipAtSetpoint(ManipulatorConstants.clearZoneSetpoint, .2)) {
        ssuper.elevatorGoTo(ElevatorConstants.L4Setpoint);
        if (ssuper.elevatorAtSetpoint(ElevatorConstants.L4Setpoint, .2)) {
          ssuper.manipGoTo(ManipulatorConstants.levelFourSetpoint);
          if (ssuper.manipAtSetpoint(ManipulatorConstants.levelFourSetpoint, .2) && !range) {
            ssuper.manipGoTo(ManipulatorConstants.clearZoneSetpoint);
            if (ssuper.manipAtSetpoint(ManipulatorConstants.clearZoneSetpoint, .2)) {
              ssuper.elevatorGoTo(ElevatorConstants.idleSetpoint);
              if (ssuper.elevatorAtSetpoint(ElevatorConstants.idleSetpoint, .2)) {
                ssuper.elevatorState = 0;
              }
            }
          }
        }
      }
    }

    if (oXCon.povDown().getAsBoolean()) {
      ssuper.manipGoTo(ManipulatorConstants.algaeRemovalSetpoint);
    }

    if (!range && !oXCon.povDown().getAsBoolean()) {
      ssuper.manipGoTo(ManipulatorConstants.intakeSetpoint);
    }

    if (range && !oXCon.povDown().getAsBoolean()) {
      ssuper.manipGoTo(ManipulatorConstants.levelTwoSetpoint);
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
