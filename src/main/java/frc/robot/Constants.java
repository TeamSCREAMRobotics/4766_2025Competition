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
  public static final class ElevatorConstants {
    // TODO: Configure motorIDs for elevator
    public static final int elevatorMasterID = 0;
    public static final int elevatorFollowerID = 0;

    // TODO: Configure SoftLimits for elevator
    public static final double elevatorForwardSoftLimit = 0.0;
    public static final double elevatorReverseSoftLimit = 0.0;

    // TODO: Configure PID for elevator
    public static final double kG = 0.0;
    public static final double kV = 0.0;
    public static final double kP = 0.0;
    public static final double kI = 0.0;
    public static final double kD = 0.0;

    // TODO: Configure MAGIC for elevator
    public static final double kMagicVelocity = 0.0;
    public static final double kMagicAcceleration = 0.0;
  }

  // Constants for the Intake Subsystem
  public static final class IntakeConstants {}

  // Constants for the Manipulator Subsystem
  public static final class ManipulatorConstants {}
}
