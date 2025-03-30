package frc.robot.constants;

import data.Length;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import zones.HexagonalPoseArea;
import zones.RectangularPoseArea;

public class FieldConstants {

  public static final Translation2d FIELD_DIMENSIONS = new Translation2d(17.548225, 8.0518);

  public static final RectangularPoseArea FIELD_AREA =
      new RectangularPoseArea(Translation2d.kZero, FIELD_DIMENSIONS);

  public static final Length BRANCH_TO_REEF_EDGE = Length.fromInches(2.111249);

  public static final Pose2d BLUE_BARGE_ALIGN =
      new Pose2d(7.715, FIELD_DIMENSIONS.getY() * 0.75, Rotation2d.fromDegrees(0));
  public static final Pose2d RED_BARGE_ALIGN =
      new Pose2d(
          FIELD_DIMENSIONS.getX() - 7.715,
          FIELD_DIMENSIONS.getY() * 0.25,
          Rotation2d.fromDegrees(180));

  public static final Translation2d BLUE_REEF_CENTER =
      new Translation2d(Units.inchesToMeters(176.746), FIELD_DIMENSIONS.getY() / 2.0);
  public static final Translation2d RED_REEF_CENTER = FIELD_DIMENSIONS.minus(BLUE_REEF_CENTER);

  public static final HexagonalPoseArea BLUE_REEF =
      new HexagonalPoseArea(BLUE_REEF_CENTER, Length.fromMeters(5), Rotation2d.fromDegrees(-30));
  public static final HexagonalPoseArea RED_REEF =
      new HexagonalPoseArea(RED_REEF_CENTER, Length.fromMeters(5), Rotation2d.fromDegrees(150));

  public static final List<Pose2d> LEFT_BLUE_REEF_LOCATIONS = new ArrayList<>();
  public static final List<Pose2d> LEFT_RED_REEF_LOCATIONS = new ArrayList<>();
  public static final List<Pose2d> RIGHT_BLUE_REEF_LOCATIONS = new ArrayList<>();
  public static final List<Pose2d> RIGHT_RED_REEF_LOCATIONS = new ArrayList<>();

  public static final Map<Integer, Pose2d> BLUE_ALGAE_LOCATIONS = new HashMap<>();
  public static final Map<Integer, Pose2d> RED_ALGAE_LOCATIONS = new HashMap<>();

  public static final List<Integer> BLUE_VALID_REEF_TAGS = List.of(22, 21, 20, 19, 18, 17);
  public static final List<Integer> RED_VALID_REEF_TAGS = List.of(6, 7, 8, 9, 10, 11);

  public static final Map<Integer, Integer> BLUE_REEF_TAGS = new HashMap<>();
  public static final Map<Integer, Integer> RED_REEF_TAGS = new HashMap<>();

  static {
    BLUE_REEF_TAGS.put(21, 0);
    BLUE_REEF_TAGS.put(20, 1);
    BLUE_REEF_TAGS.put(19, 2);
    BLUE_REEF_TAGS.put(18, 3);
    BLUE_REEF_TAGS.put(17, 4);
    BLUE_REEF_TAGS.put(22, 5);

    RED_REEF_TAGS.put(7, 0);
    RED_REEF_TAGS.put(8, 1);
    RED_REEF_TAGS.put(9, 2);
    RED_REEF_TAGS.put(10, 3);
    RED_REEF_TAGS.put(11, 4);
    RED_REEF_TAGS.put(6, 5);
  }

  private static Pair<Integer, Pose2d> getTagPair(int id) {
    return Pair.of(
        id,
        AprilTagFieldLayout.loadField(AprilTagFields.k2025ReefscapeWelded)
            .getTagPose(id)
            .get()
            .toPose2d());
  }

  // Right side of reef
  public static final Translation2d REEF_CENTER_TO_TOP_BRANCH =
      new Translation2d(Units.inchesToMeters(30.738196), Units.inchesToMeters(6.633604));
  // Left side of reef
  public static final Translation2d REEF_CENTER_TO_BOTTOM_BRANCH =
      new Translation2d(Units.inchesToMeters(30.738196), -Units.inchesToMeters(6.633604));
  // Half of center of robot + bumpers to the edge of the reef
  // TODO: Find this value!
  public static final Translation2d BRANCH_TO_ROBOT =
      new Translation2d(Units.inchesToMeters(14.647 + BRANCH_TO_REEF_EDGE.getInches() + .5), 0);

  public static final Translation2d SCORE_LOCATION_1 =
      REEF_CENTER_TO_TOP_BRANCH.plus(BRANCH_TO_ROBOT);
  public static final Translation2d SCORE_LOCATION_2 =
      REEF_CENTER_TO_BOTTOM_BRANCH.plus(BRANCH_TO_ROBOT);
  public static final Translation2d ALGAE_LOCATION = new Translation2d(SCORE_LOCATION_1.getX(), 0);

  static {
    for (int i = 0; i < 6; i++) {
      Rotation2d rotation = Rotation2d.fromDegrees(i * 60);
      Rotation2d blueRotation = Rotation2d.fromDegrees((i * 60) + 180);
      Rotation2d redRotation = Rotation2d.fromDegrees((i * 60) + 180);

      RIGHT_BLUE_REEF_LOCATIONS.add(
          new Pose2d(BLUE_REEF_CENTER.plus(SCORE_LOCATION_1.rotateBy(rotation)), blueRotation));

      LEFT_BLUE_REEF_LOCATIONS.add(
          new Pose2d(BLUE_REEF_CENTER.plus(SCORE_LOCATION_2.rotateBy(rotation)), blueRotation));

      RIGHT_RED_REEF_LOCATIONS.add(
          new Pose2d(RED_REEF_CENTER.plus(SCORE_LOCATION_1.rotateBy(rotation)), redRotation));

      LEFT_RED_REEF_LOCATIONS.add(
          new Pose2d(RED_REEF_CENTER.plus(SCORE_LOCATION_2.rotateBy(rotation)), redRotation));

      BLUE_ALGAE_LOCATIONS.put(
          i, new Pose2d(BLUE_REEF_CENTER.plus(ALGAE_LOCATION.rotateBy(rotation)), blueRotation));

      RED_ALGAE_LOCATIONS.put(
          i, new Pose2d(RED_REEF_CENTER.plus(ALGAE_LOCATION.rotateBy(rotation)), redRotation));
    }
  }
}
