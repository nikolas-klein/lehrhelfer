export type CardType = 'TEXT' | 'MATH' | 'VIDEO';

export interface Card {
  id?: number;
  title: string;
  content: string;
  type: CardType;
  mediaUrl?: string;
  orderIndex: number;
}

export interface Lesson {
  id?: number;
  title: string;
  slug: string;
  cards: Card[];
}
