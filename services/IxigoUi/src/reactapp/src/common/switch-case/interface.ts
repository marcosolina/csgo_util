export interface ICaseProps<T> {
  value?: T;
  case: T;
  children: React.ReactNode;
}

export interface ISwitchProps<T> {
  value: T;
  children: React.ReactNode;
}
