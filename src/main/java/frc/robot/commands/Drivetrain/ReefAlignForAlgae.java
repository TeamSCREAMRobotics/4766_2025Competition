// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drivetrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsytems.Elevator;
import frc.robot.subsytems.drivetrain.CommandSwerveDrivetrain;
import frc.robot.subsytems.manipulator.Manipulator;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ReefAlignForAlgae extends Command {
  private CommandSwerveDrivetrain drivetrain;

  private boolean atReefPose = false;
  private double xCord = 0.0;
  private double yCord = 0.0;
  private double rotVal = 0.0;
  private Pose2d targetPose = new Pose2d(new Translation2d(xCord, yCord), new Rotation2d(rotVal));
  private DriveToPose command;
  private boolean l3True = false;
  private Elevator elevator;
  private Manipulator manipulator;
  private boolean commandDone = false;

  /** Creates a new reefAlignForAlgae. */
  public ReefAlignForAlgae(
      CommandSwerveDrivetrain drivetrain,
      boolean L3True,
      Elevator elevator,
      Manipulator manipulator) {
    this.drivetrain = drivetrain;
    this.manipulator = manipulator;
    this.elevator = elevator;
    l3True = L3True;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain, manipulator, elevator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    xCord = drivetrain.getPose().getX() - 0.5;
    yCord = drivetrain.getPose().getY() - 0.5;
    rotVal = drivetrain.getHeading().getDegrees();

    command = new DriveToPose(drivetrain, targetPose);
    command.initialize();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (l3True) {
      manipulator.goToSetpoint(5.5);
      elevator.goToSetPoint(16);
    } else manipulator.goToSetpoint(5.5);
    command.execute();
    if (drivetrain.getPose().getX() < xCord
        && drivetrain.getPose().getY() <= yCord
        && drivetrain.getHeading().getDegrees() == rotVal) atReefPose = true;

    if (atReefPose) {
      manipulator.goToSetpoint(0);
      if (l3True) {
        elevator.goToSetPoint(0);
      }
      commandDone = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    manipulator.goToSetpoint(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return commandDone;
  }
}
