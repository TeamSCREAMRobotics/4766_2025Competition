package frc.robot;

public class Constants {

  // Constants for the Climber Subsystem
  public static final class ClimberConstants {
    // TODO: Configure motorIDs for Climber
    public static final int climberMasterID = 0;
    public static final int climberFollowerID = 0;

    // TODO: Configure SoftLimits for Climber
    public static final double climberForwardSoftLimit = 0.0;
    public static final double climberReverseSoftLimit = 0.0;

    // TODO: Configure PID for Climber
    public static final double kG = 0.0;
    public static final double kV = 0.0;
    public static final double kP = 0.0;
    public static final double kI = 0.0;
    public static final double kD = 0.0;

    // TODO: Configure MAGIC for Climber
    public static final double kMagicVelocity = 0.0;
    public static final double kMagicAcceleration = 0.0;
  }

  // Constants for the Elevator Subsystem
  public static final class ElevatorConstants {}

  // Constants for the Intake Subsystem
  public static final class IntakeConstants {
    public static int intakePIDMotorID = 10;
    public static int intakeMotorID = 11;
    public static int BeamBreakID = 12;

    public static final double intakeFowardSoftLimit = 0.0;
    public static final double intakeReverseSoftLimit = 0.0;

    public static double intakeKP = 0;
    public static double intakeKD = 0;
    public static double intakeKI = 0;
    public static double intakeKV = 0;
    public static double intakeKG = 0;

    public static double motionMagicAcceleration = 0;
    public static double motionMagicCruiseVelocity = 0;
  }

  // Constants for the Manipulator Subsystem
  public static final class ManipulatorConstants {}
}
