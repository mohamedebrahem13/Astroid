package com.example.astroid.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.astroid.R
import com.example.astroid.asteroidradar.domain.PictureOfDay
import com.squareup.picasso.Picasso

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription = imageView.context.getString(R.string.potentially_hazardous_asteroid_image)

    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription = imageView.context.getString(R.string.safe_asteroid)

    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription = imageView.context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = imageView.context.getString(R.string.not_hazardous_asteroid_image)

    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("loadimage")
fun loadImage(imageView: ImageView, PictureOfDay: PictureOfDay?) {
    PictureOfDay?.let {
        if (it.mediaType == "image") {
            Picasso.get().load(it.url).into(imageView)
            imageView.contentDescription = imageView.context.getString(
                R.string.nasa_picture_of_day_content_description_format,
                PictureOfDay.title)
        }else{
            imageView.contentDescription = imageView.context.getString(
                R.string.nasa_image_of_the_day_place_holder)
            imageView.setImageResource(R.drawable.no_image_for_today)
        }

    }
}