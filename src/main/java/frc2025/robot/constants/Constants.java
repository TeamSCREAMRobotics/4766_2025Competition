package frc2025.robot.constants;

public class Constants {
  // Constants for the Climber Subsystem
  public static final class ClimberConstants {
    // Motor IDs for climber.
    public static final int climberMasterID = 15;

    // Soft limits for the climber.
    public static final double climberForwardSoftLimit = 460;
    public static final double climberReverseSoftLimit = -2;

    // PID for Climber.
    public static final double kG = 0.0;
    public static final double kV = 0.0;
    public static final double kP = 5.0;
    public static final double kI = 0.0;
    public static final double kD = 0.0;

    // MotionMagic for Climber.
    public static final double kMagicVelocity = 500.0;
    public static final double kMagicAcceleration = 250.0;

    // Climbing setpoint
    public static final double setpointForClimb = 297;
  }

  // Constants for the Elevator Subsystem
  public static final class ElevatorConstants {
    // motorIDs for Elevator.
    public static final int elevatorMasterID = 13;
    public static final int elevatorFollowerID = 14;

    // SoftLimits for Elevator.
    public static final double elevatorForwardSoftLimit = 50;
    public static final double elevatorReverseSoftLimit = -50;

    // PID for Elevator.
    public static final double kG = 1;
    public static final double kV = 0.0;
    public static final double kP = 8;
    public static final double kI = 0.0;
    public static final double kD = 0.04;

    // MotionMagic for Elevator.
    public static final double kMagicVelocity = 480.0;
    public static final double kMagicAcceleration = 360.0;

    // Setpoints For Elevator.
    public static final double loadingSetpoint = 4.24;
    public static final double algaeFlickL3 = 15;
    public static final double L4Setpoint = 28.6;
  }

  // Constants for the Manipulator Subsystem
  public static final class ManipulatorConstants {
    // MotorIDs for Manipulator.
    public static final int feederMotorID = 9;
    public static final int pivotMotorID = 10;
    public static final int algaeMaxID = 1;

    // ID for the CANrange
    public static final int canRangeID = 0;

    // SoftLimits for Manipulator.
    public static final double climberForwardSoftLimit = 33;
    public static final double climberReverseSoftLimit = -8;

    // PID for Manipulator.
    public static final double kG = 1;
    public static final double kV = 0.0;
    public static final double kP = 15;
    public static final double kI = 0.0;
    public static final double kD = 0.0;

    // MotionMagic for Manipulator.
    public static final double kMagicVelocity = 480;
    public static final double kMagicAcceleration = 360;

    // Laser Threshold Distance for Manipulator.
    public static final double kCanRangeDistance = 0.064;

    // TODO: Add ALL setpoints for scoring and algae removal.
    public static final double intakeSetpoint = -7.0;
    public static final double levelTwoSetpoint = 0.0;
    public static final double levelThreeSetpoint = 0.0;
    public static final double levelFourSetpoint = 30.6;

    public static final double clearZoneSetpoint = 33;

    public static final double algaeRemovalSetpoint = 10.0;
  }
}
