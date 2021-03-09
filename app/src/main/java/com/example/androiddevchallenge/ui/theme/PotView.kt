/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui.theme

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.AnimateState

/**
 * @author : Eduardo Medina
 */

@Composable
fun PotContainer(controlState: AnimateState) {
    Column(
        modifier = Modifier
            .requiredWidth(120.dp)
            .requiredHeight(140.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Pot(controlState)
        Spacer(modifier = Modifier.size(10.dp))
        Fire(controlState)
    }
}

@Composable
fun Pot(controlState: AnimateState) {
    val width = LocalDensity.current.run { 120.dp.toPx() }

    val infiniteTransition = rememberInfiniteTransition()
    val pX1 by infiniteTransition.animateFloat(
        initialValue = 50f,
        targetValue = width - 40f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val pX2 by infiniteTransition.animateFloat(
        initialValue = 150f,
        targetValue = width - 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(110.dp)
    ) {

        Tip(controlState)
        Spacer(modifier = Modifier.requiredSize(2.dp))
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val startBorderX = 20f
            val startY = 100f
            val startX = 35f
            val stroke = 10f
            val radius = 30f
            val borderPath = Path().apply {
                moveTo(startBorderX, 0f)
                lineTo(startBorderX, canvasHeight - radius * 2) // -40f
                arcTo(
                    Rect(
                        startBorderX, canvasHeight - radius * 2,
                        startBorderX + radius * 2, canvasHeight
                    ),
                    180f, -90f, false
                )
                lineTo(canvasWidth - (radius * 2 + 20f), canvasHeight)
                arcTo(
                    Rect(
                        canvasWidth - (radius * 2 + 20f), canvasHeight - radius * 2,
                        canvasWidth - 20f, canvasHeight
                    ),
                    90f, -90f, false
                )
                lineTo(canvasWidth - 20f, 0f)
            }
            drawPath(borderPath, darkBlue34, style = Stroke(stroke))

            val path = Path().apply {
                moveTo(startX, startY)
                cubicTo(
                    when (controlState) {
                        AnimateState.START -> pX1
                        AnimateState.STOP -> 140f
                    },
                    -100f,
                    when (controlState) {
                        AnimateState.START -> pX2
                        AnimateState.STOP -> canvasWidth - 140f
                    },
                    startY,
                    canvasWidth - startX, startY,
                )
                lineTo(canvasWidth - startX, canvasHeight - 20f)
                lineTo(startX, canvasHeight - 20f)
                lineTo(startX, startY)
                close()
            }
            val gradient = Brush.linearGradient(
                listOf(orangeFE, magentaE8),
                Offset(0f, 0f),
                Offset(0f, canvasHeight),
                TileMode.Clamp
            )
            drawPath(path, gradient)
        }
    }
}

@Composable
fun Tip(controlState: AnimateState) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotateTransition by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val translateTransition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -5f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
    ) {
        val canvasWidth = size.width
        val radius = 40f
        withTransform({
            translate(
                top = when (controlState) {
                    AnimateState.START -> translateTransition
                    AnimateState.STOP -> 0f
                }
            )
            rotate(
                degrees = when (controlState) {
                    AnimateState.START -> rotateTransition
                    AnimateState.STOP -> 0f
                }
            )
        }) {

            drawLine(
                start = Offset(x = 0f, y = 0f),
                end = Offset(x = canvasWidth - 0f, y = 0f),
                color = darkBlue34,
                strokeWidth = 14F
            )
            drawArc(
                color = darkBlue34,
                startAngle = 0f,
                sweepAngle = -180f,
                useCenter = true,
                style = Stroke(14F),
                size = Size(radius * 2, radius * 2),
                topLeft = Offset((canvasWidth / 2) - radius, -radius)
            )
        }
    }
}

@Composable
fun Fire(controlState: AnimateState) {
    val infiniteTransition = rememberInfiniteTransition()
    val scaleX by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(3.dp)
            .scale(
                when (controlState) {
                    AnimateState.START -> scaleX
                    AnimateState.STOP -> 1f
                },
                1f
            )
            .background(magentaEF)
    )
}

@Preview("Light Theme", widthDp = 360, heightDp = 360)
@Composable
fun PotView() {
    MyTheme {
        Surface(color = MaterialTheme.colors.background) {
            PotContainer(AnimateState.START)
        }
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colors.background) {
            PotContainer(AnimateState.STOP)
        }
    }
}
