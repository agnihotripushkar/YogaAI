package me.pushkaragnihotri.yogaai.features.poseresult

data class PoseDetail(
    val name: String,
    val sanskritName: String,
    val meaning: String,
    val benefits: List<String>,
    val instructions: List<String>,
    val alignmentCues: List<String>,
    val contraindications: List<String>
)

object PoseRepository {
    fun getPoseDetail(poseName: String): PoseDetail {
        return when (poseName) {
            "Tree Pose" -> treePoseDetail
            "Warrior II" -> warriorIIDetail
            "Plank" -> plankDetail
            else -> defaultDetail(poseName)
        }
    }

    private val treePoseDetail = PoseDetail(
        name = "Tree Pose",
        sanskritName = "Vrikshasana",
        meaning = "Vrksa meaning Tree",
        benefits = listOf(
            "Stimulates the Root Chakra and balances functions of large intestines and adrenals",
            "Strengthens spine and improves balance and poise",
            "Strengthens and tones ankles, knees, legs, and buttocks",
            "Loosens hip joints, groin, and inner thighs",
            "Calming effect, improves concentration"
        ),
        instructions = listOf(
            "Stand straight, feet together.",
            "Raise hands above head, palms together.",
            "Inhale, raise right foot and place against left inner thigh.",
            "Focus on a point slightly above eye level."
        ),
        alignmentCues = listOf(
            "Foot placed against opposite inner thigh (avoid knee).",
            "Hands reaching toward ceiling, shoulders slightly lifted.",
            "Knee of upper foot pointing sideways.",
            "Spine in natural curve."
        ),
        contraindications = listOf(
            "Ankle issues",
            "Knee issues"
        )
    )

    private val warriorIIDetail = PoseDetail(
        name = "Warrior II",
        sanskritName = "Virabhadrasana II",
        meaning = "Named after the fierce warrior Virabhadra",
        benefits = listOf(
            "Strengthens and stretches legs and ankles",
            "Stretches groins, chest and lungs, shoulders",
            "Stimulates abdominal organs",
            "Increases stamina"
        ),
        instructions = listOf(
            "Stand with legs wide apart.",
            "Turn right foot out 90 degrees, left foot in slightly.",
            "Bend right knee until thigh is parallel to floor.",
            "Raise arms parallel to floor, gaze over right hand."
        ),
        alignmentCues = listOf(
            "Front knee stacked over ankle.",
            "Torso centered (don't lean forward).",
            "Shoulders relaxed away from ears."
        ),
        contraindications = listOf(
            "Diarrhea",
            "High blood pressure",
            "Neck problems (don't turn head)"
        )
    )

    private val plankDetail = PoseDetail(
        name = "Plank",
        sanskritName = "Phalakasana",
        meaning = "Phalaka meaning Plank/Board",
        benefits = listOf(
            "Strengthens the arms, wrists, and spine",
            "Tones abdomen (core)",
            "Prepares body for arm balances"
        ),
        instructions = listOf(
            "Start on hands and knees.",
            "Step feet back, legs straight.",
            "Shoulders over wrists, body in straight line."
        ),
        alignmentCues = listOf(
            "Don't let hips sag or pike up.",
            "Press floor away with hands.",
            "Engage core and thighs."
        ),
        contraindications = listOf(
            "Carpal tunnel syndrome"
        )
    )
    
    private fun defaultDetail(name: String) = PoseDetail(
        name = name,
        sanskritName = "",
        meaning = "",
        benefits = emptyList(),
        instructions = emptyList(),
        alignmentCues = emptyList(),
        contraindications = emptyList()
    )
}
