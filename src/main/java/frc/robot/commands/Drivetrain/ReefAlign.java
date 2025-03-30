// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drivetrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Logger;
import frc.robot.constants.FieldConstants;
import frc.robot.subsytems.drivetrain.CommandSwerveDrivetrain;
import util.AllianceFlipUtil;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ReefAlign extends Command {
  private CommandSwerveDrivetrain s_drivetrain;
  private boolean leftTrue = false;
  private int lastTagID = 0;
  private boolean isCurrentTag = false;
  private boolean atReefPose = false;
  private Pose2d TargetPose = new Pose2d();
  private DriveToPose command;

  /** Creates a new ReefAlign. */
  public ReefAlign(CommandSwerveDrivetrain drivetrain, boolean LeftTrue) {
    leftTrue = LeftTrue;
    s_drivetrain = drivetrain;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(s_drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    // TODO: Reference this and check for tag in controller trigger.
    // var validTags = Arrays.asList(AllianceFlipUtil.get(FieldConstants.BLUE_VALID_REEF_TAGS,
    // FieldConstants.RED_VALID_REEF_TAGS));
    // if(validTags.contains(lastTagID))

    /* if(LimelightHelpers.getTV("limelight-left")){
      lastTagID = (int) LimelightHelpers.getFiducialID("limelight-left");
    }

    if(LimelightHelpers.getTV("limelight-right")){
      lastTagID = (int) LimelightHelpers.getFiducialID("limelight-right");
    }

    if(LimelightHelpers.getTV("limelight-left") || LimelightHelpers.getTV("limelight-right")) isCurrentTag = true;

    var reefLocations =
        AllianceFlipUtil.get(FieldConstants.BLUE_REEF_LOCATIONS, FieldConstants.RED_REEF_LOCATIONS);
    var tags = AllianceFlipUtil.get(FieldConstants.BLUE_REEF_TAGS, FieldConstants.RED_REEF_TAGS); */

    if (leftTrue) {
      TargetPose =
          findClosest(
              s_drivetrain.getPose(),
              AllianceFlipUtil.get(
                      FieldConstants.LEFT_BLUE_REEF_LOCATIONS,
                      FieldConstants.LEFT_RED_REEF_LOCATIONS)
                  .toArray(Pose2d[]::new)); // reefLocations.get(tags.get(lastTagID)).getFirst();
    } else
      TargetPose =
          findClosest(
              s_drivetrain.getPose(),
              AllianceFlipUtil.get(
                      FieldConstants.RIGHT_BLUE_REEF_LOCATIONS,
                      FieldConstants.RIGHT_RED_REEF_LOCATIONS)
                  .toArray(Pose2d[]::new));
    command = new DriveToPose(s_drivetrain, TargetPose);
    command.initialize();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    command.execute();
    atReefPose = s_drivetrain.comparePose2d(TargetPose, 0.02, 0.02, 5);
    Logger.log("TargetPose", TargetPose);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return atReefPose;
  }

  public static Pose2d findClosest(Pose2d origin, Pose2d... others) {
    Pose2d closest = null;
    double smallestDistance = Double.MAX_VALUE;

    for (Pose2d point : others) {
      double distance = origin.getTranslation().getDistance(point.getTranslation());
      if (distance < smallestDistance) {
        smallestDistance = distance;
        closest = point;
      }
    }

    return closest;
  }
}
