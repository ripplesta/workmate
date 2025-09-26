# WorkMate(仮)
チャット機能付き進捗管理アプリ  

## 概要
- 自身の進捗管理や目標管理をできるWebアプリ
- OpenAIのAPIは未使用、独自のコマンド方式で実装
- チャットbot形式でタスクを登録・更新・参照可能
- フロント：Thymeleaf(HTML/CSS/JavaScript)
- バックエンド：Spring Boot
- DB： PostgreSQL
- 開発環境：Eclipse / VScode

## 主機能
- ユーザー登録/ログイン機能(Spring Security、パスワードハッシュ化)
- タスクの管理/登録/更新/削除/状態管理(未着手・進行中・完了)  
- チャットコマンド形式でのタスク管理
  - /list：タスク一覧表示(検索・ソート対応)
  - /add：タスク登録(例：/add /タイトル 勉強 /期限 2025-10-01 /優先度 高など)
  - /update：タスクの更新
  - /done /doing /todo：タスク状態変更
  - /stats：進捗統計の表示(全体・月単位・週単位など)
  - /help：コマンド一覧や詳細表示

## 動作環境
- Java 17
- Maven 3.5.6
- PostgreSQL 17

## セットアップ手順  
1.リポジトリをクローン
- git clone https://github.com/ripplesta/workmate.git
- cd workmate

2.PostgreSQLにデータベースを作成
- CREATE DATABASE workmate

3.src/main/resources/application.propertiesを環境に合わせて修正
- spring.datasource.utl=jdbc:postgresql://localhost:5432/workmate
- spring.datasource.username=【あなたのDBユーザー名】
- spring.datasource.password=【あなたのDBパスワード】
- spring.jpa.hibernate.ddl-auto=update  
注意：ご自身の環境に合わせて変更してください

4.アプリを起動
- mvn spring-boot:run

5.ブラウザでアクセス
- http://localhost:8080/

## 工夫した点
- コマンド入力時の柔軟なパース
  - 例：検索時の対象を任意順で組み合わせ入力可
  - 英語と日本語対応やある程度の表記ゆれに対応など
- 進捗状況を/statsコマンドで簡単に可視化
- カレンダー表示によるタスクの可視化
- チャット画面で少しだけ雑談のようなやりとり

## 余裕があれば追加  
- 週報レポートの自動作成  
- メールなどの通知  
- もっとチャットボットに雑談のやりとりや、自然言語で対応させたい

週ごとの進捗は[こちら](./PROGRESS.md)  
学習記録やメモは[こちら](./DEVELOPMENT.md)

