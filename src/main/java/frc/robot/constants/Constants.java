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
    public static final double kMagicVelocity = 500.0;
    public static final double kMagicAcceleration = 250.0;

    // Climbing setpoint
    public static final double setpointForClimb = 475;
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
    public static final double kG = 0.5;
    public static final double kV = 0.0;
    public static final double kP = 7;
    public static final double kI = 0.0;
    public static final double kD = 0.04;

    // MotionMagic for Elevator.
    public static final double kMagicVelocity = 360.0;
    public static final double kMagicAcceleration = 280.0;

    // Setpoints For Elevator.
    public static final double L2Setpoint = 5.7;
    public static final double L3Setpoint = 19;
  }

  // Constants for the Intake Subsystem.
  public static final class IntakeConstants {
    // Intake Motor and BeamBreakIDs.
    public static final int intakePIDMotorID = 12;
    public static final int intakeMotorID = 11;

    // Intake SoftLimits.
    public static final double intakeFowardSoftLimit = 0.0;
    public static final double intakeReverseSoftLimit = 0.0;

    // Intake PID.
    public static final double intakeKP = 8;
    public static final double intakeKD = 0;
    public static final double intakeKI = 0;
    public static final double intakeKV = 0.1;
    public static final double intakeKG = 0.5;

    // Intake MotionMagic.
    public static final double motionMagicAcceleration = 10;
    public static final double motionMagicCruiseVelocity = 15;

    // Intake Setpoint.
    public static final double intakeSetpoint = 1.26;

    // AMPS for Intake spike once algae is grabbed.
    public static final int canRangeID = 1;

    public static final double kCanRangeDistance = 0.14;
  }

  // Constants for the Manipulator Subsystem
  public static final class ManipulatorConstants {
    // MotorIDs for Manipulator.
    public static final int feederMotorID = 9;
    public static final int pivotMotorID = 10;
    public static final int canRangeID = 0;

    // SoftLimits for Manipulator.
    public static final double climberForwardSoftLimit = 7;
    public static final double climberReverseSoftLimit = 0;

    // PID for Manipulator.
    public static final double kG = 0.5;
    public static final double kV = 0.0;
    public static final double kP = 8;
    public static final double kI = 0.0;
    public static final double kD = 0.0;

    // MotionMagic for Manipulator.
    public static final double kMagicVelocity = 112;
    public static final double kMagicAcceleration = 28;

    // Laser Threshold Distance for Manipulator.
    public static final double kCanRangeDistance = 0.074;

    // TODO: Add ALL setpoints for scoring and algae removal.
    public static final double manipSetpoint = 5.6;
  }
}
