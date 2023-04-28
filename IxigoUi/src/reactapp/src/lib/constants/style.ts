import { IColor } from "./interfaces";

export const DEFAULT_SPACING = 2;

export enum NotistackVariant {
  default = "default",
  error = "error",
  success = "success",
  waring = "warning",
  info = "info",
}

export const SKELETON = {
  INPUT_TEXT_HEIGHT: 40,
};

export enum TypographyVariants {
  h1 = "h1",
  h2 = "h2",
  h3 = "h3",
  h4 = "h4",
  h5 = "h5",
  h6 = "h6",
  subtitle1 = "subtitle1",
  subtitle2 = "subtitle2",
  body1 = "body1",
  body2 = "body2",
  button = "button",
  caption = "caption",
  overline = "overline",
}

export const COLOR_PALETTE: IColor[] = [
  { red: 0, green: 54, blue: 255 }, // Blu
  { red: 255, green: 0, blue: 223 }, // Rosa
  { red: 15, green: 212, blue: 192 }, // Verde Acqua
  { red: 255, green: 123, blue: 0 }, // Arancione
  { red: 174, green: 67, blue: 67 }, // Marrone
  { red: 67, green: 141, blue: 133 }, // Verde Scuro
  { red: 186, green: 124, blue: 66 }, // Marrone Chiaro
  { red: 182, green: 176, blue: 53 }, // Oliva
  { red: 79, green: 255, blue: 0 }, // Verde evidenziatore
  { red: 255, green: 169, blue: 90 }, // Arancio chiaro
  { red: 222, green: 212, blue: 22 }, // Senape
  { red: 90, green: 163, blue: 57 }, // Verde
  { red: 183, green: 0, blue: 255 }, // Viola
  { red: 55, green: 98, blue: 36 }, // Verde molto scuro
  { red: 255, green: 104, blue: 104 }, // Rossino
  { red: 119, green: 148, blue: 255 }, // Azzurro Violetto
  { red: 48, green: 68, blue: 144 }, // Blu scuro
  { red: 56, green: 180, blue: 167 }, // Verde chiaro
  { red: 101, green: 46, blue: 122 }, // Viola Scuro
  { red: 255, green: 0, blue: 0 }, // Rosso
  { red: 0, green: 0, blue: 0 }, // Nero
];

export const generateRgbaString = (color: IColor, alpha: number): string => {
  return `rgba(${color.red},${color.green},${color.blue},${alpha})`;
};
