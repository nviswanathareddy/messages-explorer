package com.nviswanathareddy.messagesexplorer.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.nviswanathareddy.messagesexplorer.utils.AppHeaderControlSize
import com.nviswanathareddy.messagesexplorer.utils.AppPalette

@Composable
fun SearchHeaderIcon(palette: AppPalette, onClick: () -> Unit) {
    HeaderIcon(Icons.Outlined.Search, "Search", palette, onClick)
}

@Composable
fun DateHeaderIcon(palette: AppPalette, onClick: () -> Unit) {
    HeaderIcon(Icons.Outlined.CalendarMonth, "Select date", palette, onClick)
}

@Composable
fun MoreHeaderIcon(palette: AppPalette, onClick: () -> Unit) {
    HeaderIcon(Icons.Outlined.MoreVert, "More", palette, onClick)
}

@Composable
private fun HeaderIcon(
    icon: ImageVector,
    description: String,
    palette: AppPalette,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(AppHeaderControlSize)
            .clickable(onClick = onClick),
        shape = CircleShape,
        color = palette.controlBackground
    ) {
        Icon(
            icon,
            contentDescription = description,
            modifier = Modifier.padding(10.dp),
            tint = palette.primaryDark
        )
    }
    Spacer(Modifier.width(8.dp))
}
