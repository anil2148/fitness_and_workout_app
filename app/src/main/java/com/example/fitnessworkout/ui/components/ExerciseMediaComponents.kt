package com.example.fitnessworkout.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fitnessworkout.data.model.Exercise
import com.example.fitnessworkout.data.model.WorkoutPlan
import com.example.fitnessworkout.ui.theme.FitnessBlack
import com.example.fitnessworkout.ui.theme.FitnessGreen
import com.example.fitnessworkout.utils.ExerciseMedia
import androidx.compose.ui.res.stringResource
import com.example.fitnessworkout.R

@Composable
fun ExerciseIllustration(exercise: Exercise, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Image(
        painter = androidx.compose.ui.res.painterResource(ExerciseMedia.drawableId(context, exercise)),
        contentDescription = stringResource(R.string.exercise_illustration, exercise.name),
        contentScale = ContentScale.Fit,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(FitnessGreen.copy(alpha = .12f)),
    )
}

@Composable
fun WorkoutPlanIllustration(plan: WorkoutPlan, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Image(
        painter = androidx.compose.ui.res.painterResource(ExerciseMedia.drawableId(context, ExerciseMedia.planImageResourceName(plan))),
        contentDescription = stringResource(R.string.workout_illustration, plan.category),
        contentScale = ContentScale.Crop,
        modifier = modifier.clip(RoundedCornerShape(14.dp)),
    )
}

@Composable
fun ExerciseVideoPlayer(
    exercise: Exercise,
    isPremiumUser: Boolean,
    onPremium: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val packagedVideoId = ExerciseMedia.rawVideoId(context, exercise)
    ExerciseVideoPlaceholder(
        exercise = exercise,
        isLocked = !isPremiumUser,
        hasPackagedVideo = packagedVideoId != null,
        onPremium = onPremium,
        modifier = modifier,
    )
}

@Composable
fun ExerciseVideoPlaceholder(
    exercise: Exercise,
    isLocked: Boolean,
    hasPackagedVideo: Boolean = false,
    onPremium: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val description = ExerciseMedia.videoPlaceholderDescription(exercise.name)
    val content: @Composable () -> Unit = {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(contentAlignment = Alignment.Center) {
                    ExerciseIllustration(exercise, Modifier.size(width = 104.dp, height = 68.dp))
                    Icon(
                        if (isLocked) Icons.Default.Lock else Icons.Default.PlayArrow,
                        contentDescription = description,
                        tint = Color.White,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(FitnessBlack.copy(alpha = .78f))
                            .padding(5.dp),
                    )
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(exercise.name, color = Color.White, fontWeight = FontWeight.Bold)
                    Text(
                        if (isLocked) stringResource(R.string.premium_video_guide) else stringResource(R.string.video_guide_coming_soon),
                        color = FitnessGreen,
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(exercise.instruction, color = Color.LightGray, style = MaterialTheme.typography.labelSmall, maxLines = 2)
                }
            }
            LinearProgressIndicator(progress = { 0f }, modifier = Modifier.fillMaxWidth())
            Text(
                when {
                    isLocked -> stringResource(R.string.unlock_video_placeholder)
                    hasPackagedVideo -> stringResource(R.string.local_video_detected)
                    else -> stringResource(R.string.video_guide_coming_soon)
                },
                color = Color.LightGray,
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
    if (isLocked) {
        Card(
            onClick = onPremium,
            modifier = modifier.fillMaxWidth().semantics { contentDescription = description },
            shape = RoundedCornerShape(14.dp),
        ) { Box(Modifier.background(FitnessBlack), contentAlignment = Alignment.Center) { content() } }
    } else {
        Card(
            modifier = modifier.fillMaxWidth().semantics { contentDescription = description },
            shape = RoundedCornerShape(14.dp),
        ) { Box(Modifier.background(FitnessBlack), contentAlignment = Alignment.Center) { content() } }
    }
}
