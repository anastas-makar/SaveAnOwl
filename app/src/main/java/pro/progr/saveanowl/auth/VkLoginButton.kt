package pro.progr.saveanowl.auth

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VkLoginButton(
    vm: VkAuthViewModel,
    modifier: Modifier = Modifier,
    text: String = "Войти через VK ID"
) {
    Button(onClick = vm::signIn, modifier = modifier) {
        Text(text)
    }
}