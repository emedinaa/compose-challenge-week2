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
package com.example.androiddevchallenge

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PowerSettingsNew
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.PotContainer
import com.example.androiddevchallenge.ui.theme.darkBlue34
import com.example.androiddevchallenge.ui.theme.gray93
import com.example.androiddevchallenge.ui.theme.gray98
import com.example.androiddevchallenge.ui.theme.magentaCD
import com.example.androiddevchallenge.ui.theme.magentaEF
import com.example.androiddevchallenge.ui.theme.magentaF7
import kotlin.math.cos
import kotlin.math.sin

const val DEFAULT_TIME = "02 : 00"
lateinit var countDownTimer: CountDownTimer

enum class AnimateState {
    START, STOP
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

fun startTimer(updateState: (value: String, changeState: Boolean) -> Unit) {
    countDownTimer = object : CountDownTimer(2 * 60000L, 1000) {
        override fun onTick(p0: Long) {
            val minute = (p0 / 1000) / 60
            val second = (p0 / 1000) % 60
            val secondLabel = if (second < 10) "0$second" else second
            updateState("0$minute : $secondLabel", false)
        }

        override fun onFinish() {
            updateState(DEFAULT_TIME, true)
        }
    }
    countDownTimer.start()
}

fun stopTimer(updateState: (value: String) -> Unit) {
    countDownTimer.cancel()
    updateState(DEFAULT_TIME)
}

@Composable
fun View() {
    val circleGradientBrush = Brush.radialGradient(
        colors = listOf(magentaF7, magentaEF, magentaCD)
    )
    var controlState by remember { mutableStateOf(AnimateState.STOP) }
    var textState by remember { mutableStateOf(DEFAULT_TIME) }

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Spacer(modifier = Modifier.size(40.dp))
        Text(
            text = "COOKING SOUP",
            color = gray98,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = textState,
            color = darkBlue34,
            style = MaterialTheme.typography.h3,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(20.dp))
        HeaderView(controlState)
        Spacer(modifier = Modifier.size(20.dp))
        PotContainer(controlState)
        Spacer(modifier = Modifier.size(50.dp))

        IconButton(
            onClick = {
                if (controlState == AnimateState.STOP) {
                    startTimer { it, changeState ->
                        textState = it
                        if (changeState) {
                            textState = DEFAULT_TIME
                            controlState = AnimateState.START
                        }
                    }
                } else {
                    stopTimer {
                        textState = it
                    }
                }
                controlState = when (controlState) {
                    AnimateState.START -> AnimateState.STOP
                    AnimateState.STOP -> AnimateState.START
                }
            },
            Modifier
                .size(50.dp)
                .clip(CircleShape)
                .shadow(4.dp)
                .background(circleGradientBrush)
        ) {
            Icon(
                imageVector = when (controlState) {
                    AnimateState.START -> Icons.Outlined.Stop
                    AnimateState.STOP -> Icons.Outlined.PowerSettingsNew
                },
                contentDescription = "Icon",
                modifier = Modifier.requiredSize(30.dp),
                tint = Color.White
            )
        }
    }
}

@Composable
fun HeaderView(controlState: AnimateState) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotateTransition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(100.dp),
        contentAlignment = Alignment.TopCenter
    ) {

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
        ) {
            val centerH = Pair(size.width / 2, -100f)
            val radius = 400
            drawCircle(magentaEF, 16f)
            rotate(
                degrees = when (controlState) {
                    AnimateState.START -> rotateTransition
                    AnimateState.STOP -> 0f
                },
                Offset((size.width / 2), -100f)
            ) {
                for (angle in 15..360 step 15) {
                    drawCircle(
                        gray93, 10f,
                        Offset(
                            centerH.first + (radius * cos(Math.toRadians(angle.toDouble())).toFloat()),
                            centerH.second + radius * sin(Math.toRadians(angle.toDouble())).toFloat()
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun MyApp() {
    Surface(color = MaterialTheme.colors.background) {
        View()
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
