import api from '../api';
import type { Lesson } from '../types';

export const lessonService = {
  async getBySlug(slug: string): Promise<Lesson> {
    const response = await api.get<Lesson>(`/public/lessons/${slug}`);
    return response.data;
  },
  async create(lesson: Lesson): Promise<Lesson> {
    const response = await api.post<Lesson>('/public/lessons', lesson);
    return response.data;
  }
};
