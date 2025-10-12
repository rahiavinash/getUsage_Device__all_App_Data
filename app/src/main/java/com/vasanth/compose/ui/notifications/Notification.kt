package com.vasanth.compose.ui.notifications



import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vasanth.compose.R


@Composable
fun Notification(){
    Column {
        MainToolbar()
        NotificationList()
        Surface {
            InstalledAppsScreen(getSysPackages = false) // Call the composable here
        }
    }

}
@Composable
fun NotificationList(){
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .background(color = colorResource(id = R.color.white))
        .padding(20.dp)) {
        item {
            Text(text = "Today", color = colorResource(id = R.color.white))
        }
            // Define a list of notifications
            val notifications = listOf(
                NotificationData(R.drawable.ic_offline, "Limit Reached", "You've reached your daily data limit. Purchase an add-on or wait until tomorrow for renewal." , "just now"),
                NotificationData(R.drawable.ic_launcher_foreground, "Limit Reached", "You've reached your daily data limit. Purchase an add-on or wait until tomorrow for renewal.", "1 minitus ago"),
                NotificationData(R.drawable.ic_offline, "Security Alert", "You've reached your daily data limit. Purchase an add-on or wait until tomorrow for renewal.", "1 minitus ago"),
                NotificationData(R.drawable.ic_offline, "Security Alert", "You've reached your daily data limit. Purchase an add-on or wait until tomorrow for renewal.", "1 minitus ago"),
                NotificationData(R.drawable.ic_offline, "Security Alert", "You've reached your daily data limit. Purchase an add-on or wait until tomorrow for renewal.", "1 minitus ago"),
                NotificationData(R.drawable.ic_offline, "Security Alert", "You've reached your daily data limit. Purchase an add-on or wait until tomorrow for renewal.", "1 minitus ago"),
                NotificationData(R.drawable.ic_offline, "Security Alert", "You've reached your daily data limit. Purchase an add-on or wait until tomorrow for renewal.", "1 minitus ago")
            )

            items(notifications) { notification ->
                NotificationItems(
                    icon=notification.icon,
                    mainText= notification.mainText,
                    subText = notification.subText ,
                    time=notification.time)
            }
        }

    }
data class NotificationData(val icon: Int, val mainText: String, val subText: String, val time:String)


@Composable
fun NotificationItems(icon: Int, mainText: String, subText:String,time:String) {
   TextButton(onClick = {},
       modifier=Modifier.fillMaxWidth(),
       contentPadding = PaddingValues(0.dp)
   ) {
Row(
    modifier= Modifier
        .padding(5.dp)
        .fillMaxWidth()
        .height(60.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
) {
    Row (horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically){
        Icon(painter = painterResource(id = icon), contentDescription = "" ,
            modifier = Modifier.size(30.dp))
        Spacer(modifier = Modifier.width(8.dp))
            Column (){
                Text(text = mainText, fontSize = 12.sp)
                Text(text = subText, fontSize = 8.sp)
                Text(
                    text = time, fontSize = 8.sp,
                    //   color = colorResource(id = R.color.gray
                )
            }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp), // Height of the line
            color = Color.Gray // Set the color of the line
        )
    }


}

   }

}



@Composable
fun MainToolbar(){
    Row {
        Text(text = "Notification",
            fontSize = 16.sp)
    }
}

@Preview
@Composable
fun showScreen(){

    Notification()
}


@Composable
fun InstalledAppsScreen(getSysPackages: Boolean) {
    val context = LocalContext.current
    val installedApps = remember { mutableStateOf<List<PInfo>>(emptyList()) }

    LaunchedEffect(Unit) {
        installedApps.value = fetchInstalledApps(context, getSysPackages)
    }

    LazyColumn {
        items(installedApps.value) { appInfo ->
            AppItem(appInfo)
        }
    }
}

data class PInfo(
    val appName: String,
    val packageName: String,
    val versionName: String?,
    val versionCode: Int,
    val icon: Any // You may want to change this to a more specific type
)

private suspend fun fetchInstalledApps(context: Context, getSysPackages: Boolean): List<PInfo> {
    return withContext(Dispatchers.IO) {
        val res = mutableListOf<PInfo>()
        val packageManager = context.packageManager
        val packs: List<PackageInfo> = packageManager.getInstalledPackages(0)

        for (p in packs) {
            if (!getSysPackages && (p.versionName == null)) {
                continue
            }
            val appName = p.applicationInfo.loadLabel(packageManager).toString()
            val icon = p.applicationInfo.loadIcon(packageManager)

            val newInfo = PInfo(
                appName = appName,
                packageName = p.packageName,
                versionName = p.versionName,
                versionCode = p.versionCode,
                icon = icon // You can change this to an appropriate type
            )

            res.add(newInfo)
        }
        res
    }
}


@Composable
fun AppItem(appInfo: PInfo) {
    // Display the app icon, name, and version
    Text(text = appInfo.appName, fontSize = 20.sp)
    Text(text = "Package: ${appInfo.packageName}", fontSize = 14.sp)
    Text(text = "Version: ${appInfo.versionName ?: "N/A"}", fontSize = 14.sp)

    // Load icon using Coil or another image loading library
    Image(
        painter = rememberImagePainter(appInfo.icon),
        contentDescription = appInfo.appName,
        modifier = Modifier.size(48.dp)
    )
}