package com.sanket.jubifarm.cropImage.listeners;


import com.sanket.jubifarm.cropImage.enums.ImagePickerEnum;

/**
 * Ahsen Saeed}
 * ahsansaeed067@gmail.com}
 * 2/6/19}
 */

@FunctionalInterface
public interface IImagePickerLister {
    void onOptionSelected(ImagePickerEnum imagePickerEnum);
}
