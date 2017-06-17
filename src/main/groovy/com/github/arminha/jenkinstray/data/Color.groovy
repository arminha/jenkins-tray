package com.github.arminha.jenkinstray.data

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.CompileStatic

@CompileStatic
enum Color {
  @JsonProperty("red")
  Red,
  @JsonProperty("red_anime")
  RedAnime,
  @JsonProperty("yellow")
  Yellow,
  @JsonProperty("yellow_anime")
  YellowAnime,
  @JsonProperty("blue")
  Blue,
  @JsonProperty("blue_anime")
  BlueAnime,
  @JsonProperty("grey")
  Grey,
  @JsonProperty("grey_anime")
  GreyAnime,
  @JsonProperty("disabled")
  Disabled,
  @JsonProperty("disabled_anime")
  DisabledAnime,
  @JsonProperty("aborted")
  Aborted,
  @JsonProperty("aborted_anime")
  AbortedAnime,
  @JsonProperty("notbuilt")
  NotBuilt,
  @JsonProperty("notbuilt_anime")
  NotBuiltAnime,
}
