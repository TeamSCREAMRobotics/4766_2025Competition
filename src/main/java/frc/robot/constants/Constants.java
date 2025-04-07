package frc.robot.constants;

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
    public static final double kMagicVelocity = 400.0;
    public static final double kMagicAcceleration = 200.0;

    // Climbing setpoint
    public static final double setpointForClimb = 297;
  }

  // Constants for the Elevator Subsystem
  public static final class ElevatorConstants {
    // motorIDs for Elevator.
    public static final int elevatorMasterID = 13;
    public static final int elevatorFollowerID = 14;

    // SoftLimits for Elevator.
    public static final double elevatorForwardSoftLimit = 29;
    public static final double elevatorReverseSoftLimit = 0;

    // PID for Elevator.
    public static final double kG = 1;
    public static final double kV = 0.0;
    public static final double kP = 8;
    public static final double kI = 0.0;
    public static final double kD = 0.04;

    // MotionMagic for Elevator.
    public static final double kMagicVelocity = 660.0;
    public static final double kMagicAcceleration = 500.0;

    // Setpoints For Elevator.
    public static final double loadingSetpoint = 4.14;
    public static final double algaeFlickL3 = 15;
    public static final double L4Setpoint = 28.823;
    // L4 was around 28.7, increased to stop against hard stops.
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
    // 35
    public static final double climberForwardSoftLimit = 35;
    // NEW: 0.550;
    // -8
    public static final double climberReverseSoftLimit = -8;
    // NEW: -0.1;

    // PID for Manipulator.
    public static final double kG = 1;
    public static final double kV = 0.0;
    // 15
    public static final double kP = 15;
    // TODO: Tune CANCoder P: 35;
    public static final double kI = 0.0;
    public static final double kD = 0.0;
    // 0.05;

    // MotionMagic for Manipulator.
    // 480
    public static final double kMagicVelocity = 480;
    // NEW: 300;
    // 200
    public static final double kMagicAcceleration = 200;
    // NEW: 80;

    // Laser Threshold Distance for Manipulator.
    public static final double kCanRangeDistance = 0.064;

    // TODO: Add ALL setpoints for scoring and algae removal.
    // Intake was -7.0
    public static final double intakeSetpoint = -7.0;
    // NEW: -0.057;
    // L2 was 25
    public static final double levelTwoSetpoint = 25;
    // NEW: 0.375;
    // L3 was 30.9
    public static final double levelThreeSetpoint = 30.9;
    // NEW: 0.458;
    // L4 was 30
    public static final double levelFourSetpoint = 30;
    // NEW: 0.452;
    // Home was 33.5
    public static final double clearZoneSetpoint = 33.5;
    // NEW: 0.5;
    // Remove was 10.0
    public static final double algaeRemovalSetpoint = 10;
    // NEW: 0.135;
  }
}
