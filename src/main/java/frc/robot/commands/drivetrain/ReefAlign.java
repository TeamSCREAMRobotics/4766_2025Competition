// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drivetrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Logger;
import frc.robot.constants.FieldConstants;
import frc.robot.subsytems.CommandSwerveDrivetrain;
import util.AllianceFlipUtil;
import vision.LimelightHelpers;

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

    lastTagID = (int) LimelightHelpers.getFiducialID("limelight-front");
    isCurrentTag = LimelightHelpers.getTV("limelight-front");
    var reefLocations =
        AllianceFlipUtil.get(FieldConstants.BLUE_REEF_LOCATIONS, FieldConstants.RED_REEF_LOCATIONS);
    var tags = AllianceFlipUtil.get(FieldConstants.BLUE_REEF_TAGS, FieldConstants.RED_REEF_TAGS);

    if (leftTrue) {
      TargetPose = reefLocations.get(tags.get(lastTagID)).getFirst();
    } else TargetPose = reefLocations.get(tags.get(lastTagID)).getSecond();
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
    return atReefPose || !isCurrentTag;
  }
}
