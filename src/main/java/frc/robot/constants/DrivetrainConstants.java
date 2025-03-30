package frc.robot.constants;

import static edu.wpi.first.units.Units.MetersPerSecond;

import com.pathplanner.lib.config.ModuleConfig;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.controllers.PathFollowingController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.math.util.Units;
import java.util.HashMap;
import pid.ScreamPIDConstants;
import util.PPUtil;

public final class DrivetrainConstants {
  public static final double MAX_SPEED = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond);
  public static final double MAX_ANGULAR_SPEED_RADS = 8.0;
  public static final double MAX_AZIMUTH_VEL_RADS = Units.rotationsToRadians(10);

  public static final int NUM_MODULES = 4;

  public static final ScreamPIDConstants HEADING_CORRECTION_CONSTANTS =
      new ScreamPIDConstants(8.0, 0.0, 0.0);

  // TODO: Tune this PIDController for DriveToPose.
  public static final ProfiledPIDController DRIVE_ALIGNMENT_CONTROLLER =
      new ProfiledPIDController(3.20, 0.0, 0.001, new Constraints(5, 4));

  public static final PIDController HEADING_CONTROLLER =
      HEADING_CORRECTION_CONSTANTS.getPIDController(-Math.PI, Math.PI);

  public static final ScreamPIDConstants PATH_TRANSLATION_CONSTANTS =
      new ScreamPIDConstants(10.0, 0.0, 0.0);
  public static final ScreamPIDConstants PATH_ROTATION_CONSTANTS =
      new ScreamPIDConstants(7.0, 0.0, 0.0);

  public static final ModuleConfig MODULE_CONFIG =
      new ModuleConfig(Units.inchesToMeters(2), MAX_SPEED, 1.4, DCMotor.getKrakenX60(1), 85, 1);

  public static final RobotConfig ROBOT_CONFIG =
      new RobotConfig(
          Units.lbsToKilograms(150), 6.883, MODULE_CONFIG, TunerConstants.TRACK_WIDTH.getMeters());

  public static final PathFollowingController PATH_FOLLOWING_CONTROLLER =
      new PPHolonomicDriveController(
          PPUtil.screamPIDConstantsToPPConstants(PATH_TRANSLATION_CONSTANTS),
          PPUtil.screamPIDConstantsToPPConstants(PATH_ROTATION_CONSTANTS));

  public static final HashMap<Integer, Pose2d> ReefPose = new HashMap<>();

  static {
    // Give this map an ID (An April Tag in this case) and is will return the given Pose2d with the
    // ID.
    ReefPose.put(1, new Pose2d(120, 120, Rotation2d.fromDegrees(0.0)));
  }
}
