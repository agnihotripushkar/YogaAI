package me.pushkaragnihotri.yogaai.features.yoga.data.source

import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import me.pushkaragnihotri.yogaai.features.yoga.domain.model.PoseClassification
import kotlin.math.abs
import kotlin.math.atan2

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
            return PoseClassification("No Pose Detected", 0f, feedback = "Please ensure your full body is visible.")
        }

        val person = landmarks[0]

        // Check for Tree Pose
        val treePoseCheck = checkTreePose(person)
        if (treePoseCheck.isCorrect) {
            return PoseClassification("Tree Pose", 0.9f, true, "Great balance! Keep your core engaged.")
        } else if (treePoseCheck.confidence > 0.5f) {
             return PoseClassification("Tree Pose", 0.6f, false, treePoseCheck.feedback)
        }

        // Check for Warrior II
        val warriorCheck = checkWarriorII(person)
        if (warriorCheck.isCorrect) {
            return PoseClassification("Warrior II", 0.9f, true, "Strong stance! Look over your front hand.")
        } else if (warriorCheck.confidence > 0.5f) {
            return PoseClassification("Warrior II", 0.6f, false, warriorCheck.feedback)
        }

        // Check for Plank
        val plankCheck = checkPlank(person)
        if (plankCheck.isCorrect) {
            return PoseClassification("Plank", 0.8f, true, "Solid plank! Keep your hips aligned.")
        } else if (plankCheck.confidence > 0.5f) {
             return PoseClassification("Plank", 0.6f, false, plankCheck.feedback)
        }

        return PoseClassification("Detecting...", 0.5f, feedback = "Keep moving to find a pose.")
    }

    private data class CheckResult(val isCorrect: Boolean, val feedback: String, val confidence: Float = 0f)

    private fun checkTreePose(landmarks: List<com.google.mediapipe.tasks.components.containers.NormalizedLandmark>): CheckResult {
        val leftKneeAngle = calculateAngle(landmarks[LEFT_HIP], landmarks[LEFT_KNEE], landmarks[LEFT_ANKLE])
        val rightKneeAngle = calculateAngle(landmarks[RIGHT_HIP], landmarks[RIGHT_KNEE], landmarks[RIGHT_ANKLE])

        val oneKneeBent = (leftKneeAngle < 100f && rightKneeAngle > 160f) ||
                          (rightKneeAngle < 100f && leftKneeAngle > 160f)
        
        return if (oneKneeBent) {
            CheckResult(true, "", 0.9f)
        } else {
             CheckResult(false, "Bend one knee and place your foot on the inner thigh of the other leg.", 0.6f)
        }
    }

    private fun checkWarriorII(landmarks: List<com.google.mediapipe.tasks.components.containers.NormalizedLandmark>): CheckResult {
        val leftShoulderAngle = calculateAngle(landmarks[LEFT_ELBOW], landmarks[LEFT_SHOULDER], landmarks[LEFT_HIP])
        val rightShoulderAngle = calculateAngle(landmarks[RIGHT_ELBOW], landmarks[RIGHT_SHOULDER], landmarks[RIGHT_HIP])

        val armsHorizontal = leftShoulderAngle in 70f..110f && rightShoulderAngle in 70f..110f
        
        return if (armsHorizontal) {
            CheckResult(true, "", 0.9f)
        } else {
            CheckResult(false, "Raise your arms to be parallel with the floor.", 0.6f)
        }
    }

    private fun checkPlank(landmarks: List<com.google.mediapipe.tasks.components.containers.NormalizedLandmark>): CheckResult {
        val shoulderHipKneeAngle = calculateAngle(landmarks[LEFT_SHOULDER], landmarks[LEFT_HIP], landmarks[LEFT_KNEE])
        
        return if (shoulderHipKneeAngle > 160f) {
            CheckResult(true, "", 0.9f)
        } else {
            CheckResult(false, "Straighten your body. Don't let your hips sag or pike up.", 0.6f)
        }
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
