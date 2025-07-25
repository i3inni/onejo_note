export interface Schedule {
  day: number
  subject: string
}

export const todaySchedules: Schedule[] = [
  { day: 22, subject: '데이터베이스 조별 발표 계획' },
  { day: 23, subject: '데이터베이스 조별 발표' },
  { day: 24, subject: '데이터베이스 조별 발표2' },
  { day: 25, subject: '프로젝트 발표' },
  { day: 26, subject: 'MySQL 예제 풀이' },
  { day: 27, subject: '관계 데이터 학습' },
  { day: 28, subject: 'MySQL subquery' },
  { day: 29, subject: 'MySQL 인덱스' },
  { day: 30, subject: 'MySQL 뷰' },
  { day: 31, subject: 'NULL값 처리' },
]
