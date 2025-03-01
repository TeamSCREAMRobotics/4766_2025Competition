// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants.IntakeConstants;
import frc.robot.subsytems.Intake;
import java.util.function.BooleanSupplier;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class runIntake extends Command {
  private final Intake intake;
  private final BooleanSupplier buttonPressed;
  private boolean hasAlgae = false;
  private double intakeAlgaeSetpoint = IntakeConstants.intakeSetpoint;
  private double currentThreshold = IntakeConstants.kAmps;
  private final double intakeIn;

  /** Creates a new runIntake. */
  public runIntake(Intake intake, BooleanSupplier buttonPressed, double intakeIn) {
    this.intake = intake;
    this.buttonPressed = buttonPressed;
    this.intakeIn = intakeIn;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    intake.goToSetpoint(0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    intake.goToSetpoint(-0.133);

    if (intake.amperageSpiked(currentThreshold)) {
      hasAlgae = true;
    }

    if (buttonPressed.getAsBoolean()) {
      if (!hasAlgae) {
        intake.goToSetpoint(intakeAlgaeSetpoint);
        intake.runFlywheel(-intakeIn);
        System.out.print("buttonPressed");
      } else {
        intake.runFlywheel(5);
      }
    } else {
      intake.resetFlywheel();
      intake.goToSetpoint(-0.133);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.resetFlywheel();
    intake.goToSetpoint(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
