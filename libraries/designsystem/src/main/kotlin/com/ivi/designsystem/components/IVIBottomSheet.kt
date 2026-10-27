package com.ivi.designsystem.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ivi.designsystem.theme.IVIColors
import com.ivi.designsystem.theme.IVIShapes
import com.ivi.designsystem.theme.IVISpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IVIBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = IVIShapes.ExtraLarge,
        containerColor = IVIColors.SurfaceVariant,
        contentColor = IVIColors.OnSurface,
        scrimColor = IVIColors.Scrim,
        modifier = modifier
    ) {
        content()
    }
}
