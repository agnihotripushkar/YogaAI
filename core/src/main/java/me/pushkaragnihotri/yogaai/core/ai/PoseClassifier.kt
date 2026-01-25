package me.pushkaragnihotri.yogaai.core.ai

import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.toDegrees

data class PoseClassification(
    val poseName: String,
    val confidence: Float,
    val isCorrect: Boolean = false
)

class PoseClassifier {

    companion object {
        const val NOSE = 0
        const val LEFT_SHOULDER = 11
        const val RIGHT_SHOULDER = 12
        const val LEFT_ELBOW = 13
        const val RIGHT_ELBOW = 14
        const val LEFT_WRIST = 15
        const val RIGHT_WRIST = 16
        const val LEFT_HIP = 23
        const val RIGHT_HIP = 24
        const val LEFT_KNEE = 25
        const val RIGHT_KNEE = 26
        const val LEFT_ANKLE = 27
        const val RIGHT_ANKLE = 28
    }

    fun classify(result: PoseLandmarkerResult): PoseClassification {
        val landmarks = result.landmarks()
        if (landmarks.isEmpty() || landmarks[0].isEmpty()) {
            return PoseClassification("No Pose Detected", 0f)
        }

        val person = landmarks[0]

        // Check for Tree Pose
        if (isTreePose(person)) {
            return PoseClassification("Tree Pose", 0.9f, true)
        }

        // Check for Warrior II
        if (isWarriorII(person)) {
            return PoseClassification("Warrior II", 0.9f, true)
        }

        // Check for Plank
        if (isPlank(person)) {
            return PoseClassification("Plank", 0.8f, true)
        }

        return PoseClassification("Detecting...", 0.5f)
    }

    private fun isTreePose(landmarks: List<com.google.mediapipe.tasks.components.containers.NormalizedLandmark>): Boolean {
        val leftKneeAngle = calculateAngle(
            landmarks[LEFT_HIP],
            landmarks[LEFT_KNEE],
            landmarks[LEFT_ANKLE]
        )
        val rightKneeAngle = calculateAngle(
            landmarks[RIGHT_HIP],
            landmarks[RIGHT_KNEE],
            landmarks[RIGHT_ANKLE]
        )

        // In Tree Pose, one knee is bent significantly (e.g., < 90 degrees)
        // while the other is straight (> 160 degrees)
        val oneKneeBent = (leftKneeAngle < 100f && rightKneeAngle > 160f) ||
                          (rightKneeAngle < 100f && leftKneeAngle > 160f)
        
        return oneKneeBent
    }

    private fun isWarriorII(landmarks: List<com.google.mediapipe.tasks.components.containers.NormalizedLandmark>): Boolean {
        val leftShoulderAngle = calculateAngle(
            landmarks[LEFT_ELBOW],
            landmarks[LEFT_SHOULDER],
            landmarks[LEFT_HIP]
        )
        val rightShoulderAngle = calculateAngle(
            landmarks[RIGHT_ELBOW],
            landmarks[RIGHT_SHOULDER],
            landmarks[RIGHT_HIP]
        )

        // Arms should be roughly horizontal (around 90 degrees from the body)
        val armsHorizontal = leftShoulderAngle in 70f..110f && rightShoulderAngle in 70f..110f
        
        return armsHorizontal
    }

    private fun isPlank(landmarks: List<com.google.mediapipe.tasks.components.containers.NormalizedLandmark>): Boolean {
        val shoulderHipKneeAngle = calculateAngle(
            landmarks[LEFT_SHOULDER],
            landmarks[LEFT_HIP],
            landmarks[LEFT_KNEE]
        )
        
        // Body should be in a straight line (around 180 degrees)
        return shoulderHipKneeAngle > 160f
    }

    private fun calculateAngle(
        a: com.google.mediapipe.tasks.components.containers.NormalizedLandmark,
        b: com.google.mediapipe.tasks.components.containers.NormalizedLandmark,
        c: com.google.mediapipe.tasks.components.containers.NormalizedLandmark
    ): Double {
        val radians = atan2(c.y() - b.y(), c.x() - b.x()) - 
                      atan2(a.y() - b.y(), a.x() - b.x())
        var angle = abs(radians * 180.0 / Math.PI)
        if (angle > 180.0) {
            angle = 360.0 - angle
        }
        return angle
    }
}
