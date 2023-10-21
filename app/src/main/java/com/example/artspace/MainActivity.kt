package com.example.artspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artspace.ui.theme.ArtSpaceTheme

class MainActivity : ComponentActivity() {
    private val listOfArt =
        listOf(
            Art(1, R.drawable.mona_lisa, "Mona Lisa", "Leonardo da Vinci", 1519),
            Art(2, R.drawable.the_last_supper, "The Last Supper", "Leonardo da Vinci", 1498),
            Art(3, R.drawable.the_starry_night, "The Starry Night", "Vincent van Gogh", 1889),
            Art(4, R.drawable.the_scream, "The Scream", "Edvard Munch", 1893),
            Art(5, R.drawable.guernica, "Guernica", "Pablo Picasso", 1937),
            Art(6, R.drawable.the_kiss, "The Kiss", "Gustav Klimt", 1908),
            Art(
                7,
                R.drawable.girl_with_a_pearl_earring,
                "Girl With a Pearl Earring",
                "Johannes Vermeer",
                1665
            ),
            Art(8, R.drawable.the_birth_of_venus, "The Birth of Venus", "Sandro Botticelli", 1485),
            Art(9, R.drawable.las_meninas, "Las Meninas", "Diego Velázquez", 1656),
            Art(10, R.drawable.creation_of_adam, "Creation of Adam", "Michelangelo", 1512)
        )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtSpaceTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ArtSpaceMain(listOfArt = listOfArt)
                }
            }
        }
    }
}

@Composable
fun ArtSpaceMain(listOfArt: List<Art>) {
    var currentArt by remember {
        mutableStateOf(listOfArt[0])
    }
    var offsetX by remember { mutableStateOf(0f) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .background(Color.White),
        Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .background(Color.LightGray)
                .padding(20.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        if (offsetX < -300) {
                            // Swiped right (Previous)
                            currentArt = onPreviousButtonClicked(listOfArt, currentArt)
                            offsetX = 0f
                        } else if (offsetX > 300) {
                            // Swiped left (Next)
                            currentArt = onNextButtonClicked(listOfArt, currentArt)
                            offsetX = 0f
                        }
                    }
                },
            horizontalAlignment = CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = currentArt.src),
                contentDescription = currentArt.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = CenterHorizontally
        ) {
            Text(text = currentArt.title, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(currentArt.artist)
                }

                append("   (${currentArt.year})")
            })
        }

        Row(Modifier.wrapContentWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = {
                currentArt = onPreviousButtonClicked(listOfArt, currentArt)
            }, Modifier.offset(x = (-5).dp)) {
                Text(text = "Previous")
            }

            Button(
                onClick = { currentArt = onNextButtonClicked(listOfArt, currentArt) },
                Modifier.offset(x = 5.dp)
            ) {
                Text(text = "Next")
            }
        }
    }
}

private fun onPreviousButtonClicked(listOfArt: List<Art>, currentArt: Art): Art {
    val targetID = when (currentArt.ID) {
        1 -> 10
        in 2..10 -> currentArt.ID - 1
        else -> 1
    }
    return listOfArt[targetID - 1]
}

private fun onNextButtonClicked(listOfArt: List<Art>, currentArt: Art): Art {
    val targetID = when (currentArt.ID) {
        10 -> 1
        in 1..9 -> currentArt.ID + 1
        else -> 1
    }
    return listOfArt[targetID - 1]
}

@Preview
@Composable
fun ArtSpaceMainPreview() {
    ArtSpaceMain(
        listOfArt = listOf(
            Art(1, R.drawable.mona_lisa, "Mona Lisa", "Leonardo da Vinci", 1519),
            Art(2, R.drawable.the_last_supper, "The Last Supper", "Leonardo da Vinci", 1498),
            Art(3, R.drawable.the_starry_night, "The Starry Night", "Vincent van Gogh", 1889)
        )
    )
}